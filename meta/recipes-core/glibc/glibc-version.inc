SRCBRANCH ?= "release/2.44/master"
PV = "2.44+git"
SRCREV_glibc ?= "afd131806b25715a7617ab757db43f0bb610acbf"
SRCREV_localedef ?= "cba02c503d7c853a38ccfb83c57e343ca5ecd7e5"

GLIBC_GIT_URI ?= "git://sourceware.org/git/glibc.git;protocol=https"

UPSTREAM_CHECK_GITTAGREGEX = "(?P<pver>\d+\.\d+(\.(?!90)\d+)*)"
