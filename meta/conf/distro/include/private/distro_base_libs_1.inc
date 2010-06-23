RECIPE_STATUS_pn-icu = "yellow" #do_configure for autotool;icu-3.6.inc
DEPENDENCY_CHECK_pn-icu = "not done"
RECIPE_LATEST_VERSION_pn-dbus-wait = "3_6"
RECIPE_PATCH_pn-icu+gccfix = "to be pushed upstream"
RECIPE_PATCH_pn-icu+use-g++-for-linking = "Unclear"
RECIPE_INTEL_SECTION_pn-icu = "base libs"
RECIPE_TIME_BETWEEN_LAST_TWO_RELEASES_pn-icu = "n/a"
RECIPE_LATEST_RELEASE_DATE_pn-icu = "08/2006"
RECIPE_COMMENTS_pn-icu = "arm optimizations; license similar to MIT, however sub-license is not allowed; autoreconf is not used and current style is not clean. need enhancement in the future"

RECIPE_STATUS_pn-eggdbus = "yellow" # patch comment
DEPENDENCY_CHECK_pn-eggdbus = "not done"
RECIPE_LATEST_VERSION_pn-eggdbus = "0.6"
RECIPE_PATCH_pn-eggdbus+gtk-doc = "local; no comment; poky doesn't have full gtk-doc support, and eggdbus itself references AM_CONDITIONAL in Makefile.am earlier before configure script is run"
RECIPE_PATCH_pn-eggdbus+marshal = "local; no comment; one binary generated in eggdbus build needs to be invoked. So point to native version"
RECIPE_INTEL_SECTION_pn-eggdbus = "base libs"
RECIPE_TIME_BETWEEN_LAST_TWO_RELEASES_pn-eggdbus = "4 months"
RECIPE_LATEST_RELEASE_DATE_pn-eggdbus = "11/2009"
RECIPE_COMMENTS_pn-eggdbus = "http://mail.gnome.org/archives/gtk-devel-list/2008-December/msg00059.html contains more detail introduction"

RECIPE_STATUS_pn+libdaemon = "green"
DEPENDENCY_CHECK_pn+libdaemon = "not done"
RECIPE_LATEST_VERSION_pn+libdaemon = "0.14"
RECIPE_INTEL_SECTION_pn+libdaemon = "base libs"
RECIPE_TIME_BETWEEN_LAST_TWO_RELEASES_pn+libdaemon = "15 months"
RECIPE_LATEST_RELEASE_DATE_pn+libdaemon = "11/2009"
RECIPE_COMMENTS_pn+libdaemon = ""

