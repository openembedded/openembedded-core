SRCBRANCH ?= "release/2.40/master"
PV = "2.40+git"
SRCREV_glibc ?= "7073164add3b874cf0c19ca0fb84236f6bb0985d"
SRCREV_localedef ?= "fab74f31b3811df543e24b6de47efdf45b538abc"

GLIBC_GIT_URI ?= "git://sourceware.org/git/glibc.git;protocol=https"

UPSTREAM_CHECK_GITTAGREGEX = "(?P<pver>\d+\.\d+(\.(?!90)\d+)*)"
