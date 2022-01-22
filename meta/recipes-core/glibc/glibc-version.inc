SRCBRANCH ?= "release/2.33/master"
PV = "2.33"
SRCREV_glibc ?= "55b99e9ed07688019609bd4dcd17d3ebf4572948"
SRCREV_localedef ?= "bd644c9e6f3e20c5504da1488448173c69c56c28"

GLIBC_GIT_URI ?= "git://sourceware.org/git/glibc.git"

UPSTREAM_CHECK_GITTAGREGEX = "(?P<pver>\d+\.\d+(\.(?!90)\d+)*)"
