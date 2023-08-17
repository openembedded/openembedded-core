SRCBRANCH ?= "release/2.38/master"
PV = "2.38"
SRCREV_glibc ?= "36f2487f13e3540be9ee0fb51876b1da72176d3f"
SRCREV_localedef ?= "e0eca29583b9e0f62645c4316ced93cf4e4e26e1"

GLIBC_GIT_URI ?= "git://sourceware.org/git/glibc.git;protocol=https"

UPSTREAM_CHECK_GITTAGREGEX = "(?P<pver>\d+\.\d+(\.(?!90)\d+)*)"
