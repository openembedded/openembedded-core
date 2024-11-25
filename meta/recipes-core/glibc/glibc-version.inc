SRCBRANCH ?= "release/2.39/master"
PV = "2.39+git"
SRCREV_glibc ?= "dcaf51b41e259387602774829c45222d0507f90a"
SRCREV_localedef ?= "fab74f31b3811df543e24b6de47efdf45b538abc"

GLIBC_GIT_URI ?= "git://sourceware.org/git/glibc.git;protocol=https"

UPSTREAM_CHECK_GITTAGREGEX = "(?P<pver>\d+\.\d+(\.(?!90)\d+)*)"
