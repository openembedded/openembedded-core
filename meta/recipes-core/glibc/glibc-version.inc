SRCBRANCH ?= "release/2.38/master"
PV = "2.38"
SRCREV_glibc ?= "36f2487f13e3540be9ee0fb51876b1da72176d3f"
SRCREV_localedef ?= "2d6d138500779504ba52e564edd21a3e50ae9472"

GLIBC_GIT_URI ?= "git://sourceware.org/git/glibc.git;protocol=https"

UPSTREAM_CHECK_GITTAGREGEX = "(?P<pver>\d+\.\d+(\.(?!90)\d+)*)"
