SRCBRANCH ?= "release/2.31/master"
PV = "2.31"
SRCREV_glibc ?= "9ea3686266dca3f004ba874745a4087a89682617"
SRCREV_localedef ?= "cd9f958c4c94a638fa7b2b4e21627364f1a1a655"

GLIBC_GIT_URI ?= "git://sourceware.org/git/glibc.git"

UPSTREAM_CHECK_GITTAGREGEX = "(?P<pver>\d+\.\d+(\.(?!90)\d+)*)"
