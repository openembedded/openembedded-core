SRCBRANCH ?= "release/2.39/master"
PV = "2.39+git"
SRCREV_glibc ?= "ce65d944e38a20cb70af2a48a4b8aa5d8fabe1cc"
SRCREV_localedef ?= "cba02c503d7c853a38ccfb83c57e343ca5ecd7e5"

GLIBC_GIT_URI ?= "git://sourceware.org/git/glibc.git;protocol=https"

UPSTREAM_CHECK_GITTAGREGEX = "(?P<pver>\d+\.\d+(\.(?!90)\d+)*)"
