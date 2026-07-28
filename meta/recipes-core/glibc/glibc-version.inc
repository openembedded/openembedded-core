SRCBRANCH ?= "release/2.44/master"
PV = "2.44+git"
SRCREV_glibc ?= "7cba77790f3279bec3ac20e9c7632b021cd53f95"
SRCREV_localedef ?= "cba02c503d7c853a38ccfb83c57e343ca5ecd7e5"

GLIBC_GIT_URI ?= "git://sourceware.org/git/glibc.git;protocol=https"

UPSTREAM_CHECK_GITTAGREGEX = "(?P<pver>\d+\.\d+(\.(?!90)\d+)*)"
