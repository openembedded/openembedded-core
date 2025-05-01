#
# Copyright OpenEmbedded Contributors
#
# SPDX-License-Identifier: MIT
#

# This class is used by the yocto-check-layer script for additional
# per-recipe tests.
#
# It adds an anonymous python function with extra processing to all recipes,
# globally inheriting this class isn't advisable - yocto-check-layer script
# handles that during its signature dump


# Ensure that recipes don't skip required QA checks as listed
# in CHECKLAYER_REQUIRED_TESTS, defined by insane.bbclass
def check_insane_skip(d):
    required_tests = set((d.getVar('CHECKLAYER_REQUIRED_TESTS') or '').split())
    packages = set((d.getVar('PACKAGES') or '').split())
    for package in packages:
        skip = set((d.getVar('INSANE_SKIP') or "").split() +
                   (d.getVar('INSANE_SKIP:' + package) or "").split())
        skip_required = skip & required_tests
        if skip_required:
            oe.qa.write_error(" ".join(skip_required), 'Package %s is skipping required QA tests.' % package, d)
            bb.error("QA Issue: %s [%s]" % ('Package %s is skipping required QA tests.' % package, " ".join(skip_required)))
            d.setVar("QA_ERRORS_FOUND", "True")


python () {
    check_insane_skip(d)
}
