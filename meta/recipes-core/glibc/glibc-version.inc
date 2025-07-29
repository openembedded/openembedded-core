SRCBRANCH ?= "release/2.39/master"
PV = "2.39+git"
SRCREV_glibc ?= "b027d5b145f1b2908f370bdb96dfe40180d0fcb6"
SRCREV_localedef ?= "fab74f31b3811df543e24b6de47efdf45b538abc"

GLIBC_GIT_URI ?= "git://sourceware.org/git/glibc.git;protocol=https"

UPSTREAM_CHECK_GITTAGREGEX = "(?P<pver>\d+\.\d+(\.(?!90)\d+)*)"
