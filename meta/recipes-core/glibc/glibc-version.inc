SRCBRANCH ?= "release/2.32/master"
PV = "2.32"
SRCREV_glibc ?= "3de512be7ea6053255afed6154db9ee31d4e557a"
SRCREV_localedef ?= "b6ca7c146c6d2c93cddfa61e395cb8a196e3d46f"

GLIBC_GIT_URI ?= "git://sourceware.org/git/glibc.git"

UPSTREAM_CHECK_GITTAGREGEX = "(?P<pver>\d+\.\d+(\.(?!90)\d+)*)"
