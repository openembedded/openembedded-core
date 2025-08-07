SRCBRANCH ?= "release/2.41/master"
PV = "2.41+git"
SRCREV_glibc ?= "e7c419a2957590fb657900fc92a89708f41abd9d"
SRCREV_localedef ?= "fab74f31b3811df543e24b6de47efdf45b538abc"

GLIBC_GIT_URI ?= "git://sourceware.org/git/glibc.git;protocol=https"

UPSTREAM_CHECK_GITTAGREGEX = "(?P<pver>\d+\.\d+(\.(?!90)\d+)*)"
