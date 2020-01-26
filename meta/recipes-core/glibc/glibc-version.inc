SRCBRANCH ?= "master"
PV = "2.30.9000"
SRCREV_glibc ?= "352bb99754ae7c83ff1b974f9c52244e974c9410"
SRCREV_localedef ?= "cd9f958c4c94a638fa7b2b4e21627364f1a1a655"

GLIBC_GIT_URI ?= "git://sourceware.org/git/glibc.git"

UPSTREAM_CHECK_GITTAGREGEX = "(?P<pver>\d+\.\d+(\.(?!90)\d+)*)"
