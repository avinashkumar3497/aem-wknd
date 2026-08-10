(function (document, $) {
    "use strict";

    // Register a custom validator with Granite's validation framework
    $(window).adaptTo("foundation-registry").register("foundation.validation.validator", {

        // Only run this validator on fields tagged with data-validation="pdf-only"
        selector: "[data-validation='pdf-only']",

        // The actual check. `el` is the field element being validated.
        validate: function (el) {

            // A pathfield is a composite widget, so the selected path
            // usually lives on an inner <input>. Read that value.
            var $el   = $(el);
            var value = $el.val() || $el.find("input").val();

            // If a value is present AND it does NOT end with ".pdf",
            // return an error message -> this blocks saving and shows the message.
            if (value && value.toLowerCase().lastIndexOf(".pdf") !== (value.length - 4)) {
                return "Please select a PDF file only.";
            }

            // Returning nothing (undefined) means: valid, no error.
        }
    });

})(document, Granite.$);