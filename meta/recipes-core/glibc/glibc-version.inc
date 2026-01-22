SRCBRANCH ?= "release/2.42/master"
PV = "2.42+git"
SRCREV_glibc ?= "453e6b8dbab935257eb0802b0c97bca6b67ba30e"
SRCREV_localedef ?= "cba02c503d7c853a38ccfb83c57e343ca5ecd7e5"

GLIBC_GIT_URI ?= "git://sourceware.org/git/glibc.git;protocol=https"

UPSTREAM_CHECK_GITTAGREGEX = "(?P<pver>\d+\.\d+(\.(?!90)\d+)*)"
