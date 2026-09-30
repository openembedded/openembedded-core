SRCBRANCH ?= "release/2.43/master"
PV = "2.43+git"
SRCREV_glibc ?= "9cda6fc96abd035d9cbe68482138d4a78a51a7d5"
SRCREV_localedef ?= "cba02c503d7c853a38ccfb83c57e343ca5ecd7e5"

GLIBC_GIT_URI ?= "git://sourceware.org/git/glibc.git;protocol=https"

UPSTREAM_CHECK_GITTAGREGEX = "(?P<pver>\d+\.\d+(\.(?!90)\d+)*)"
