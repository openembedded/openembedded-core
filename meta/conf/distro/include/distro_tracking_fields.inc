RECIPE_STATUS_pn+icu = "yellow" #do_configure for autotool;icu-3.6.inc
DEPENDENCY_CHECK_pn+icu = "not done"
RECIPE_LATEST_VERSION_pn+dbus-wait = "3_6"
RECIPE_PATCH_pn+icu+gccfix = "to be pushed upstream"
RECIPE_PATCH_pn+icu+use-g++-for-linking = "Unclear"
RECIPE_INTEL_SECTION_pn+icu = "base lib"
RECIPE_TIME_BETWEEN_LAST_TWO_RELEASES_pn+icu = "n/a"
RECIPE_LATEST_RELEASE_DATE_pn+icu = "08/2006"
RECIPE_COMMENTS_pn+icu = "arm optimizations; license similar to MIT, however sub-license is not allowed"

ECIPE_STATUS_pn+dbus-wait = "green"
DEPENDENCY_CHECK_pn+dbus-wait = "not done"
RECIPE_LATEST_VERSION_pn+dbus-wait = "402"
RECIPE_INTEL_SECTION_pn+dbus-wait = "base util"
RECIPE_TIME_BETWEEN_LAST_TWO_RELEASES_pn+dbus-wait = "n/a"
RECIPE_LATEST_RELEASE_DATE_pn+dbus-wait = "08/2008"
RECIPE_COMMENTS_pn+dbus-wait = "o-hand project"

RECIPE_STATUS_pn+e2fsprogs = "green"
DEPENDENCY_CHECK_pn+e2fsprogs = "not done"
RECIPE_LATEST_VERSION_pn+e2fsprogs = "1.41.12"
RECIPE_PATCH_pn+e2fsprogs+quotefix = "to be pushed upstream"
RECIPE_PATCH_pn+e2fsprogs+mkdir = "Unclear"
RECIPE_PATCH_pn+e2fsprogs+acinclude = "Unclear"
RECIPE_INTEL_SECTION_pn+e2fsprogs = "base util"
RECIPE_TIME_BETWEEN_LAST_TWO_RELEASES_pn+e2fsprogs = "2 months"
RECIPE_LATEST_RELEASE_DATE_pn+e2fsprogs = "05/2010"
RECIPE_COMMENTS_pn+e2fsprogs = ""

RECIPE_STATUS_pn+binutils="yellow" # custom do_configure for autotools recipe in the binutils.inc file
RECIPE_LATEST_VERSION_pn+binutils="2.20.1"
RECIPE_NO_OF_PATCHES_pn+binutils="8"
RECIPE_TIME_BETWEEN_LAST_TWO_RELEASES_pn+binutils="1 months"
RECIPE_LATEST_RELEASE_DATE_pn+binutils="2010/05/28"

RECIPE_STATUS_pn+gcc="red" # recipe building is failing
RECIPE_LATEST_VERSION_pn+gcc="4.5.0"
RECIPE_NO_OF_PATCHES_pn+gcc="8"
RECIPE_TIME_BETWEEN_LAST_TWO_RELEASES_pn+gcc="5 months"
RECIPE_LATEST_RELEASE_DATE_pn+gcc="2010/03/03"

RECIPE_STATUS_pn+autoconf="green" # no update needed
RECIPE_LATEST_VERSION_pn+autoconf="2.65"
RECIPE_NO_OF_PATCHES_pn+autoconf="10"
RECIPE_TIME_BETWEEN_LAST_TWO_RELEASES_pn+autoconf="4 months"
RECIPE_LATEST_RELEASE_DATE_pn+autoconf="2009/11/21"

RECIPE_STATUS_pn+automake="green" # no update needed
RECIPE_LATEST_VERSION_pn+automake="1.11.1"
RECIPE_NO_OF_PATCHES_pn+automake="1"
RECIPE_TIME_BETWEEN_LAST_TWO_RELEASES_pn+automake="7 months"
RECIPE_LATEST_RELEASE_DATE_pn+automake="2009/12/08"

RECIPE_STATUS_pn+bison="red" # upgrade needed
RECIPE_LATEST_VERSION_pn+bison="2.4.2"
RECIPE_NO_OF_PATCHES_pn+bison="1"
RECIPE_TIME_BETWEEN_LAST_TWO_RELEASES_pn+bison="15 months"
RECIPE_LATEST_RELEASE_DATE_pn+bison="2010/03/20"

RECIPE_STATUS_pn+diffstat="red" # upgrade needed
RECIPE_LATEST_VERSION_pn+diffstat="1.51"
RECIPE_NO_OF_PATCHES_pn+diffstat="0"
RECIPE_TIME_BETWEEN_LAST_TWO_RELEASES_pn+diffstat="1 months"
RECIPE_LATEST_RELEASE_DATE_pn+diffstat="2009/11/07"

RECIPE_STATUS_pn+distcc="green" # no update needed
RECIPE_LATEST_VERSION_pn+distcc="2.18.3"
RECIPE_NO_OF_PATCHES_pn+distcc="2"
RECIPE_TIME_BETWEEN_LAST_TWO_RELEASES_pn+distcc="1 months"
RECIPE_LATEST_RELEASE_DATE_pn+distcc="2004/11/30"

RECIPE_STATUS_pn+exmap-console="green" # no update needed
RECIPE_LATEST_VERSION_pn+exmap-console="0.4.1"
RECIPE_NO_OF_PATCHES_pn+exmap-console="0"
RECIPE_TIME_BETWEEN_LAST_TWO_RELEASES_pn+exmap-console="2 months"
RECIPE_LATEST_RELEASE_DATE_pn+exmap-console="2007/02/26"

RECIPE_STATUS_pn+flex="red" # upgrade needed
RECIPE_LATEST_VERSION_pn+flex="2.5.35"
RECIPE_NO_OF_PATCHES_pn+flex="3"
RECIPE_TIME_BETWEEN_LAST_TWO_RELEASES_pn+flex="2 months"
RECIPE_LATEST_RELEASE_DATE_pn+flex="2008/02/26"

RECIPE_STATUS_pn+gdb="red" # depricating to meta-extra
RECIPE_LATEST_VERSION_pn+fstests="???"
RECIPE_NO_OF_PATCHES_pn+fstests="0"
RECIPE_TIME_BETWEEN_LAST_TWO_RELEASES_pn+fstests="???"
RECIPE_LATEST_RELEASE_DATE_pn+fstests="???"

RECIPE_STATUS_pn+gdb="red" # upgrade needed
RECIPE_LATEST_VERSION_pn+gdb="7.1"
RECIPE_NO_OF_PATCHES_pn+gdb="8"
RECIPE_TIME_BETWEEN_LAST_TWO_RELEASES_pn+gdb="3 months"
RECIPE_LATEST_RELEASE_DATE_pn+gdb="2010/03/18"

RECIPE_STATUS_pn+gtk-theme-torturer="green" # no update needed git tree
RECIPE_LATEST_VERSION_pn+gtk-theme-torturer="???"
RECIPE_NO_OF_PATCHES_pn+gtk-theme-torturer="0"
RECIPE_TIME_BETWEEN_LAST_TWO_RELEASES_pn+gtk-theme-torturer="???"
RECIPE_LATEST_RELEASE_DATE_pn+gtk-theme-torturer="???"

RECIPE_STATUS_pn+intltool="red" # upgrade needed
RECIPE_LATEST_VERSION_pn+intltool="0.40.6"
RECIPE_NO_OF_PATCHES_pn+intltool="1"
RECIPE_TIME_BETWEEN_LAST_TWO_RELEASES_pn+intltool="5 months"
RECIPE_LATEST_RELEASE_DATE_pn+intltool="2009/03/14"

RECIPE_STATUS_pn+libtool="red" # upgrade needed
RECIPE_LATEST_VERSION_pn+libtool="2.2.8"
RECIPE_NO_OF_PATCHES_pn+libtool="5"
RECIPE_TIME_BETWEEN_LAST_TWO_RELEASES_pn+libtool="7 months"
RECIPE_LATEST_RELEASE_DATE_pn+libtool="2010/06/04"

RECIPE_STATUS_pn+linux-libc-headers="red" # upgrade needed
RECIPE_LATEST_VERSION_pn+linux-libc-headers="2.6.34"
RECIPE_NO_OF_PATCHES_pn+linux-libc-headers="1"
RECIPE_TIME_BETWEEN_LAST_TWO_RELEASES_pn+linux-libc-headers="3 months"
RECIPE_LATEST_RELEASE_DATE_pn+linux-libc-headers="2010/05/16"

RECIPE_STATUS_pn+lsof="red" # upgrade needed
RECIPE_LATEST_VERSION_pn+lsof="4.83"
RECIPE_NO_OF_PATCHES_pn+lsof="0"
RECIPE_TIME_BETWEEN_LAST_TWO_RELEASES_pn+lsof="10 months"
RECIPE_LATEST_RELEASE_DATE_pn+lsof="2010/01/18"

RECIPE_STATUS_pn+lttng-control="red" # upgrade needed
RECIPE_LATEST_VERSION_pn+lttng-control="0.86"
RECIPE_NO_OF_PATCHES_pn+lttng-control="0"
RECIPE_TIME_BETWEEN_LAST_TWO_RELEASES_pn+lttng-control="0 days"
RECIPE_LATEST_RELEASE_DATE_pn+lttng-control="2010/05/19"

RECIPE_STATUS_pn+lttng-viewer="red" # upgrade needed
RECIPE_LATEST_VERSION_pn+lttng-viewer="0.8.84"
RECIPE_NO_OF_PATCHES_pn+lttng-viewer="0"
RECIPE_TIME_BETWEEN_LAST_TWO_RELEASES_pn+lttng-viewer="2 months"
RECIPE_LATEST_RELEASE_DATE_pn+lttng-viewer="2007/08/12"


