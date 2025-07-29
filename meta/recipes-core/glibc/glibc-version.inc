SRCBRANCH ?= "release/2.42/master"
PV = "2.42+git"
SRCREV_glibc ?= "d2097651cc57834dbfcaa102ddfacae0d86cfb66"
SRCREV_localedef ?= "cba02c503d7c853a38ccfb83c57e343ca5ecd7e5"

GLIBC_GIT_URI ?= "git://sourceware.org/git/glibc.git;protocol=https"

UPSTREAM_CHECK_GITTAGREGEX = "(?P<pver>\d+\.\d+(\.(?!90)\d+)*)"
