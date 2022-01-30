SRCBRANCH ?= "release/2.33/master"
PV = "2.33"
SRCREV_glibc ?= "3e2a15c666e40e5ee740e5079c56d83469280323"
SRCREV_localedef ?= "bd644c9e6f3e20c5504da1488448173c69c56c28"

GLIBC_GIT_URI ?= "git://sourceware.org/git/glibc.git"

UPSTREAM_CHECK_GITTAGREGEX = "(?P<pver>\d+\.\d+(\.(?!90)\d+)*)"
