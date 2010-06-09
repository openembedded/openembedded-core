RECIPE_STATUS_pn-icu = "yellow" #do_configure for autotool;icu-3.6.inc
DEPENDENCY_CHECK_pn-icu = "not done"
RECIPE_LATEST_VERSION_pn-dbus-wait = "3_6"
RECIPE_PATCH_pn-icu+gccfix = "to be pushed upstream"
RECIPE_PATCH_pn-icu+use-g++-for-linking = "Unclear"
RECIPE_INTEL_SECTION_pn-icu = "base lib"
RECIPE_TIME_BETWEEN_LAST_TWO_RELEASES_pn-icu = "n/a"
RECIPE_LATEST_RELEASE_DATE_pn-icu = "08/2006"
RECIPE_COMMENTS_pn-icu = "arm optimizations; license similar to MIT, however sub-license is not allowed"

ECIPE_STATUS_pn-dbus-wait = "green"
DEPENDENCY_CHECK_pn-dbus-wait = "not done"
RECIPE_LATEST_VERSION_pn-dbus-wait = "402"
RECIPE_INTEL_SECTION_pn-dbus-wait = "base util"
RECIPE_TIME_BETWEEN_LAST_TWO_RELEASES_pn-dbus-wait = "n/a"
RECIPE_LATEST_RELEASE_DATE_pn-dbus-wait = "08/2008"
RECIPE_COMMENTS_pn-dbus-wait = "o-hand project"

RECIPE_STATUS_pn-e2fsprogs = "green"
DEPENDENCY_CHECK_pn-e2fsprogs = "not done"
RECIPE_LATEST_VERSION_pn-e2fsprogs = "1.41.12"
RECIPE_PATCH_pn-e2fsprogs+quotefix = "to be pushed upstream"
RECIPE_PATCH_pn-e2fsprogs+mkdir = "Unclear"
RECIPE_PATCH_pn-e2fsprogs+acinclude = "Unclear"
RECIPE_INTEL_SECTION_pn-e2fsprogs = "base util"
RECIPE_TIME_BETWEEN_LAST_TWO_RELEASES_pn-e2fsprogs = "2 months"
RECIPE_LATEST_RELEASE_DATE_pn-e2fsprogs = "05/2010"
RECIPE_COMMENTS_pn-e2fsprogs = ""

RECIPE_STATUS_pn-binutils="yellow" # custom do_configure for autotools recipe in the binutils.inc file
RECIPE_LATEST_VERSION_pn-binutils="2.20.1"
RECIPE_NO_OF_PATCHES_pn-binutils="8"
RECIPE_TIME_BETWEEN_LAST_TWO_RELEASES_pn-binutils="1 months"
RECIPE_LATEST_RELEASE_DATE_pn-binutils="2010/05/28"

RECIPE_STATUS_pn-gcc="red" # recipe building is failing
RECIPE_LATEST_VERSION_pn-gcc="4.5.0"
RECIPE_NO_OF_PATCHES_pn-gcc="8"
RECIPE_TIME_BETWEEN_LAST_TWO_RELEASES_pn-gcc="5 months"
RECIPE_LATEST_RELEASE_DATE_pn-gcc="2010/03/03"

RECIPE_STATUS_pn-autoconf="green" # no update needed
RECIPE_LATEST_VERSION_pn-autoconf="2.65"
RECIPE_NO_OF_PATCHES_pn-autoconf="10"
RECIPE_TIME_BETWEEN_LAST_TWO_RELEASES_pn-autoconf="4 months"
RECIPE_LATEST_RELEASE_DATE_pn-autoconf="2009/11/21"

RECIPE_STATUS_pn-automake="green" # no update needed
RECIPE_LATEST_VERSION_pn-automake="1.11.1"
RECIPE_NO_OF_PATCHES_pn-automake="1"
RECIPE_TIME_BETWEEN_LAST_TWO_RELEASES_pn-automake="7 months"
RECIPE_LATEST_RELEASE_DATE_pn-automake="2009/12/08"

RECIPE_STATUS_pn-bison="red" # upgrade needed
RECIPE_LATEST_VERSION_pn-bison="2.4.2"
RECIPE_NO_OF_PATCHES_pn-bison="1"
RECIPE_TIME_BETWEEN_LAST_TWO_RELEASES_pn-bison="15 months"
RECIPE_LATEST_RELEASE_DATE_pn-bison="2010/03/20"

RECIPE_STATUS_pn-diffstat="red" # upgrade needed
RECIPE_LATEST_VERSION_pn-diffstat="1.51"
RECIPE_NO_OF_PATCHES_pn-diffstat="0"
RECIPE_TIME_BETWEEN_LAST_TWO_RELEASES_pn-diffstat="1 months"
RECIPE_LATEST_RELEASE_DATE_pn-diffstat="2009/11/07"

RECIPE_STATUS_pn-distcc="green" # no update needed
RECIPE_LATEST_VERSION_pn-distcc="2.18.3"
RECIPE_NO_OF_PATCHES_pn-distcc="2"
RECIPE_TIME_BETWEEN_LAST_TWO_RELEASES_pn-distcc="1 months"
RECIPE_LATEST_RELEASE_DATE_pn-distcc="2004/11/30"

RECIPE_STATUS_pn-exmap-console="green" # no update needed
RECIPE_LATEST_VERSION_pn-exmap-console="0.4.1"
RECIPE_NO_OF_PATCHES_pn-exmap-console="0"
RECIPE_TIME_BETWEEN_LAST_TWO_RELEASES_pn-exmap-console="2 months"
RECIPE_LATEST_RELEASE_DATE_pn-exmap-console="2007/02/26"

RECIPE_STATUS_pn-flex="red" # upgrade needed
RECIPE_LATEST_VERSION_pn-flex="2.5.35"
RECIPE_NO_OF_PATCHES_pn-flex="3"
RECIPE_TIME_BETWEEN_LAST_TWO_RELEASES_pn-flex="2 months"
RECIPE_LATEST_RELEASE_DATE_pn-flex="2008/02/26"

RECIPE_STATUS_pn-gdb="red" # depricating to meta-extra
RECIPE_LATEST_VERSION_pn-fstests="???"
RECIPE_NO_OF_PATCHES_pn-fstests="0"
RECIPE_TIME_BETWEEN_LAST_TWO_RELEASES_pn-fstests="???"
RECIPE_LATEST_RELEASE_DATE_pn-fstests="???"

RECIPE_STATUS_pn-gdb="red" # upgrade needed
RECIPE_LATEST_VERSION_pn-gdb="7.1"
RECIPE_NO_OF_PATCHES_pn-gdb="8"
RECIPE_TIME_BETWEEN_LAST_TWO_RELEASES_pn-gdb="3 months"
RECIPE_LATEST_RELEASE_DATE_pn-gdb="2010/03/18"

RECIPE_STATUS_pn-gtk-theme-torturer="green" # no update needed git tree
RECIPE_LATEST_VERSION_pn-gtk-theme-torturer="???"
RECIPE_NO_OF_PATCHES_pn-gtk-theme-torturer="0"
RECIPE_TIME_BETWEEN_LAST_TWO_RELEASES_pn-gtk-theme-torturer="???"
RECIPE_LATEST_RELEASE_DATE_pn-gtk-theme-torturer="???"

RECIPE_STATUS_pn-intltool="red" # upgrade needed
RECIPE_LATEST_VERSION_pn-intltool="0.40.6"
RECIPE_NO_OF_PATCHES_pn-intltool="1"
RECIPE_TIME_BETWEEN_LAST_TWO_RELEASES_pn-intltool="5 months"
RECIPE_LATEST_RELEASE_DATE_pn-intltool="2009/03/14"

RECIPE_STATUS_pn-libtool="red" # upgrade needed
RECIPE_LATEST_VERSION_pn-libtool="2.2.8"
RECIPE_NO_OF_PATCHES_pn-libtool="5"
RECIPE_TIME_BETWEEN_LAST_TWO_RELEASES_pn-libtool="7 months"
RECIPE_LATEST_RELEASE_DATE_pn-libtool="2010/06/04"

RECIPE_STATUS_pn-linux-libc-headers="red" # upgrade needed
RECIPE_LATEST_VERSION_pn-linux-libc-headers="2.6.34"
RECIPE_NO_OF_PATCHES_pn-linux-libc-headers="1"
RECIPE_TIME_BETWEEN_LAST_TWO_RELEASES_pn-linux-libc-headers="3 months"
RECIPE_LATEST_RELEASE_DATE_pn-linux-libc-headers="2010/05/16"

RECIPE_STATUS_pn-lsof="red" # upgrade needed
RECIPE_LATEST_VERSION_pn-lsof="4.83"
RECIPE_NO_OF_PATCHES_pn-lsof="0"
RECIPE_TIME_BETWEEN_LAST_TWO_RELEASES_pn-lsof="10 months"
RECIPE_LATEST_RELEASE_DATE_pn-lsof="2010/01/18"

RECIPE_STATUS_pn-lttng-control="red" # upgrade needed
RECIPE_LATEST_VERSION_pn-lttng-control="0.86"
RECIPE_NO_OF_PATCHES_pn-lttng-control="0"
RECIPE_TIME_BETWEEN_LAST_TWO_RELEASES_pn-lttng-control="0 days"
RECIPE_LATEST_RELEASE_DATE_pn-lttng-control="2010/05/19"

RECIPE_STATUS_pn-lttng-viewer="red" # upgrade needed
RECIPE_LATEST_VERSION_pn-lttng-viewer="0.8.84"
RECIPE_NO_OF_PATCHES_pn-lttng-viewer="0"
RECIPE_TIME_BETWEEN_LAST_TWO_RELEASES_pn-lttng-viewer="2 months"
RECIPE_LATEST_RELEASE_DATE_pn-lttng-viewer="2007/08/12"

RECIPE_STATUS_pn-make="green" # already at the latest version
RECIPE_LATEST_VERSION_pn-make="3.81"
RECIPE_NO_OF_PATCHES_pn-make="1"
RECIPE_TIME_BETWEEN_LAST_TWO_RELEASES_pn-make="42 months"
RECIPE_LATEST_RELEASE_DATE_pn-make="2006/04/01"

RECIPE_STATUS_pn-run-postinsts="green" # all local code
RECIPE_LATEST_VERSION_pn-make="1.0"
RECIPE_NO_OF_PATCHES_pn-make="2"
RECIPE_TIME_BETWEEN_LAST_TWO_RELEASES_pn-make="6 months"
RECIPE_LATEST_RELEASE_DATE_pn-make="2008/05/20"

RECIPE_STATUS_pn-staging-linkage="green" # no code
RECIPE_LATEST_VERSION_pn-staging-linkage="1.0"
RECIPE_NO_OF_PATCHES_pn-staging-linkage="0"
RECIPE_TIME_BETWEEN_LAST_TWO_RELEASES_pn-staging-linkage="2 months"
RECIPE_LATEST_RELEASE_DATE_pn-staging-linkage="2009/11/19"

RECIPE_STATUS_pn-nasm="red" # upgrade needed
RECIPE_LATEST_VERSION_pn-nasm="2.07"
RECIPE_NO_OF_PATCHES_pn-nasm="0"
RECIPE_TIME_BETWEEN_LAST_TWO_RELEASES_pn-nasm="1 months"
RECIPE_LATEST_RELEASE_DATE_pn-nasm="2009/07/21"

RECIPE_STATUS_pn-oprofile="red" # upgrade needed
RECIPE_LATEST_VERSION_pn-oprofile="0.9.6"
RECIPE_NO_OF_PATCHES_pn-oprofile="2"
RECIPE_TIME_BETWEEN_LAST_TWO_RELEASES_pn-oprofile="4 months"
RECIPE_LATEST_RELEASE_DATE_pn-oprofile="2009/11/24"

RECIPE_STATUS_pn-perl="red" # upgrade needed
RECIPE_LATEST_VERSION_pn-perl="5.13.1"
RECIPE_NO_OF_PATCHES_pn-perl="33"
RECIPE_TIME_BETWEEN_LAST_TWO_RELEASES_pn-perl="3 days"
RECIPE_LATEST_RELEASE_DATE_pn-perl="2010/05/20"

RECIPE_STATUS_pn-prelink="red" # upgrade needed
RECIPE_LATEST_VERSION_pn-prelink="20090925"
RECIPE_NO_OF_PATCHES_pn-prelink="3"
RECIPE_TIME_BETWEEN_LAST_TWO_RELEASES_pn-prelink="6 months"
RECIPE_LATEST_RELEASE_DATE_pn-prelink="2009/09/25"

RECIPE_STATUS_pn-time = "green"
DEPENDENCY_CHECK_pn-time = "not done"
RECIPE_LATEST_VERSION_pn-time = "1.7"
RECIPE_INTEL_SECTION_pn-time = "base util"
RECIPE_TIME_BETWEEN_LAST_TWO_RELEASES_pn-time = "n/a"
RECIPE_LATEST_RELEASE_DATE_pn-time = "07/1996"
RECIPE_COMMENTS_pn-time = ""

RECIPE_STATUS_pn-gdbm = "yellow"   # custom do_configure
DEPENDENCY_CHECK_pn-gdbm = "not done"
RECIPE_LATEST_VERSION_pn-gdbm = "1.8.3"
RECIPE_PATCH_pn-gdbm+makefile = "use oe generic DESTDIR instead of package specific INSTALL_ROOT"
RECIPE_PATCH_pn-gdbm+libtool-mode = "explict specify install mode in libtool"
RECIPE_INTEL_SECTION_pn-gdbm = "base libs"
RECIPE_TIME_BETWEEN_LAST_TWO_RELEASES_pn-gdbm = "n/a"
RECIPE_LATEST_RELEASE_DATE_pn-gdbm = "10/2002"
RECIPE_COMMENTS_pn-gdbm = ""

RECIPE_STATUS_pn-python-pycurl = "yellow"  # several exports to work with python
DEPENDENCY_CHECK_pn-python-pycurl = "not done"
RECIPE_LATEST_VERSION_pn-python-pycurl = "7.19.0"
RECIPE_PATCH_pn-python-pycurl+no-static-link = "no static libraries"
RECIPE_INTEL_SECTION_pn-python-pycurl = "base libs"
RECIPE_TIME_BETWEEN_LAST_TWO_RELEASES_pn-python-pycurl = "2 months"
RECIPE_LATEST_RELEASE_DATE_pn-python-pycurl = "09/2008"
RECIPE_COMMENTS_pn-python-pycurl = "the so called MIT/X here is actually ISC license, depends on libcurl (> 7.19.0)"

RECIPE_STATUS_pn-bzip2 = "yellow"  # local config scripts
DEPENDENCY_CHECK_pn-bzip2 = "not done"
RECIPE_LATEST_VERSION_pn-bzip2 = "1.0.5"
RECIPE_PATCH_pn-python-bzip2+configure.ac = "override default build method "
RECIPE_PATCH_pn-python-bzip2+Makefile.am = "override default build method"
RECIPE_INTEL_SECTION_pn-bzip2 = "base utils"
RECIPE_TIME_BETWEEN_LAST_TWO_RELEASES_pn-bzip2 = "12 months"
RECIPE_LATEST_RELEASE_DATE_pn-bzip2 = "03/2008"
RECIPE_COMMENTS_pn-bzip2 = "bzip2 license is 3-clause BSD plus an renaming clause"

RECIPE_STATUS_pn-portmap = "yellow"
DEPENDENCY_CHECK_pn-portmap = "not done"
RECIPE_LATEST_VERSION_pn-portmap = "6.0"
RECIPE_PATCH_pn-python-portmap+dest-dir-no-strip = "override default build method "
RECIPE_PATCH_pn-python-no-libwrap = "override default build method "
RECIPE_PATCH_pn-python-no-tcpd-support = "override default build method "
RECIPE_INTEL_SECTION_pn-portmap = "base utils"
RECIPE_TIME_BETWEEN_LAST_TWO_RELEASES_pn-portmap = "n/a"
RECIPE_LATEST_RELEASE_DATE_pn-portmap = "05/2007"
RECIPE_COMMENTS_pn-portmap = "most part is under 4-clause BSD, Sun RPC code is under another license, \
however debian portmap package licenses RPC code under 3-clause BSD. This needs further investigation"
