SRCBRANCH ?= "release/2.44/master"
PV = "2.44+git"
SRCREV_glibc ?= "f715e9491c062bbb50d32ccb05a8debc488dda39"
SRCREV_localedef ?= "cba02c503d7c853a38ccfb83c57e343ca5ecd7e5"

GLIBC_GIT_URI ?= "git://sourceware.org/git/glibc.git;protocol=https"

UPSTREAM_CHECK_GITTAGREGEX = "(?P<pver>\d+\.\d+(\.(?!90)\d+)*)"
