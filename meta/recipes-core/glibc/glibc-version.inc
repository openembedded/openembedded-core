SRCBRANCH ?= "release/2.35/master"
PV = "2.35"
SRCREV_glibc ?= "d2febe7c407665c18cfea1930c65f41899ab3aa3"
SRCREV_localedef ?= "794da69788cbf9bf57b59a852f9f11307663fa87"

GLIBC_GIT_URI ?= "git://sourceware.org/git/glibc.git"

UPSTREAM_CHECK_GITTAGREGEX = "(?P<pver>\d+\.\d+(\.(?!90)\d+)*)"
