SRCBRANCH ?= "release/2.37/master"
PV = "2.37"
SRCREV_glibc ?= "b4e23c75aea756b4bddc4abcf27a1c6dca8b6bd3"
SRCREV_localedef ?= "794da69788cbf9bf57b59a852f9f11307663fa87"

GLIBC_GIT_URI ?= "git://sourceware.org/git/glibc.git;protocol=https"

UPSTREAM_CHECK_GITTAGREGEX = "(?P<pver>\d+\.\d+(\.(?!90)\d+)*)"
