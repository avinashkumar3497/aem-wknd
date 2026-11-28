(function () {
    "use strict";

    // Set up ONE instance of the component
    function init(wrapper) {
        const input = wrapper.querySelector(".cmp-locationfinder__input");
        const button = wrapper.querySelector(".cmp-locationfinder__button");
        const resultsDiv = wrapper.querySelector(".cmp-locationfinder__results");
        const servletUrl = wrapper.dataset.servletUrl;   // data-servlet-url

        if (!input || !button || !resultsDiv || !servletUrl) {
            return;
        }

        // Show a single message in the results area
        function showMessage(text) {
            resultsDiv.innerHTML = "";
            const p = document.createElement("p");
            p.textContent = text;
            resultsDiv.appendChild(p);
        }

        // Build the result list
        function renderResults(data) {
            const results = data.results;               // missing when nothing matches
            if (!Array.isArray(results) || results.length === 0) {
                showMessage("No locations found.");
                return;
            }

            const ul = document.createElement("ul");
            results.forEach(function (loc) {
                const li = document.createElement("li");
                li.textContent = loc.name
                    + (loc.country ? ", " + loc.country : "")
                    + " (Lat: " + loc.latitude + ", Lon: " + loc.longitude + ")";
                ul.appendChild(li);
            });

            resultsDiv.innerHTML = "";
            resultsDiv.appendChild(ul);
        }

        // Call the servlet
        async function search() {
            const query = input.value.trim();
            if (query === "") {
                showMessage("Please enter a city name.");
                return;
            }

            showMessage("Loading...");
            button.disabled = true;                     // no double clicks

            try {
                const response = await fetch(servletUrl + "?query=" + encodeURIComponent(query));
                if (!response.ok) {                     // fetch doesn't reject on 400/500
                    throw new Error("HTTP " + response.status);
                }
                const data = await response.json();
                renderResults(data);
            } catch (err) {
                console.error("Location Finder error:", err);
                showMessage("Something went wrong. Please try again.");
            } finally {
                button.disabled = false;
            }
        }

        button.addEventListener("click", search);
        input.addEventListener("keydown", function (e) {
            if (e.key === "Enter") {
                search();
            }
        });
    }

    // Set up every instance on the page
    function initAll() {
        document.querySelectorAll(".cmp-locationfinder1").forEach(init);
    }

    if (document.readyState === "loading") {
        document.addEventListener("DOMContentLoaded", initAll);
    } else {
        initAll();                                      // DOM already parsed
    }
})();