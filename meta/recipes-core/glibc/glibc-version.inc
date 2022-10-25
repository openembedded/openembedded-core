SRCBRANCH ?= "release/2.35/master"
PV = "2.35"
SRCREV_glibc ?= "8d125a1f9145ad90c94e438858d6b5b7578686f2"
SRCREV_localedef ?= "794da69788cbf9bf57b59a852f9f11307663fa87"

GLIBC_GIT_URI ?= "git://sourceware.org/git/glibc.git"

UPSTREAM_CHECK_GITTAGREGEX = "(?P<pver>\d+\.\d+(\.(?!90)\d+)*)"
