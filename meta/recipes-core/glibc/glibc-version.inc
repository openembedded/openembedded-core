SRCBRANCH ?= "release/2.35/master"
PV = "2.35"
SRCREV_glibc ?= "549d8315791aa8176ff1537db3e09c185c6e602f"
SRCREV_localedef ?= "794da69788cbf9bf57b59a852f9f11307663fa87"

GLIBC_GIT_URI ?= "git://sourceware.org/git/glibc.git"

UPSTREAM_CHECK_GITTAGREGEX = "(?P<pver>\d+\.\d+(\.(?!90)\d+)*)"
