SRCBRANCH ?= "release/2.36/master"
PV = "2.36"
SRCREV_glibc ?= "302bc33bc53c787da6e74162a7092e9c0fb964a8"
SRCREV_localedef ?= "794da69788cbf9bf57b59a852f9f11307663fa87"

GLIBC_GIT_URI ?= "git://sourceware.org/git/glibc.git"

UPSTREAM_CHECK_GITTAGREGEX = "(?P<pver>\d+\.\d+(\.(?!90)\d+)*)"
