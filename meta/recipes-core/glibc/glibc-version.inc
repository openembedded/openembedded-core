SRCBRANCH ?= "release/2.35/master"
PV = "2.35"
SRCREV_glibc ?= "37214df5f103f4075cf0a79a227e70f3e064701c"
SRCREV_localedef ?= "794da69788cbf9bf57b59a852f9f11307663fa87"

GLIBC_GIT_URI ?= "git://sourceware.org/git/glibc.git"

UPSTREAM_CHECK_GITTAGREGEX = "(?P<pver>\d+\.\d+(\.(?!90)\d+)*)"
