SRCBRANCH ?= "release/2.39/master"
PV = "2.39+git"
SRCREV_glibc ?= "e8f521709731ce3ae8d6f1eca30135d5c0606f02"
SRCREV_localedef ?= "fab74f31b3811df543e24b6de47efdf45b538abc"

GLIBC_GIT_URI ?= "git://sourceware.org/git/glibc.git;protocol=https"

UPSTREAM_CHECK_GITTAGREGEX = "(?P<pver>\d+\.\d+(\.(?!90)\d+)*)"

CVE_STATUS[CVE-2023-4911] = "fixed-version: Fixed in stable branch updates"
