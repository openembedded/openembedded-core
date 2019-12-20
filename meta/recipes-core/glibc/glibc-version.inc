SRCBRANCH ?= "master"
PV = "2.30.9000"
SRCREV_glibc ?= "def9c08c94da60bfb746644d56cffbe7a9de3477"
SRCREV_localedef ?= "cd9f958c4c94a638fa7b2b4e21627364f1a1a655"

GLIBC_GIT_URI ?= "git://sourceware.org/git/glibc.git"

UPSTREAM_CHECK_GITTAGREGEX = "(?P<pver>\d+\.\d+(\.(?!90)\d+)*)"
