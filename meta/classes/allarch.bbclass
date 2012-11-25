#
# This class is used for architecture independent recipes/data files (usally scripts)
#

PACKAGE_ARCH = "all"

python () {
    # Allow this class to be included but overridden - only set
    # the values if we're still "all" package arch.
    if d.getVar("PACKAGE_ARCH") == "all":
        # No need for virtual/libc or a cross compiler
        d.setVar("INHIBIT_DEFAULT_DEPS","1")

        # Set these to a common set of values, we shouldn't be using them other that for WORKDIR directory
        # naming anyway
        d.setVar("TARGET_ARCH", "allarch")
        d.setVar("TARGET_OS", "linux")
        d.setVar("TARGET_CC_ARCH", "none")
        d.setVar("TARGET_LD_ARCH", "none")
        d.setVar("TARGET_AS_ARCH", "none")
        d.setVar("PACKAGE_EXTRA_ARCHS", "")

        # No need to do shared library processing or debug symbol handling
        d.setVar("EXCLUDE_FROM_SHLIBS", "1")
        d.setVar("INHIBIT_PACKAGE_DEBUG_SPLIT", "1")
        d.setVar("INHIBIT_PACKAGE_STRIP", "1")
}

