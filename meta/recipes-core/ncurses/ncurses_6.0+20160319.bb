require ncurses.inc

SRC_URI += "file://tic-hang.patch \
            file://config.cache \
"
# commit id corresponds to the revision in package version
SRCREV = "05af40f7d3af1e43144ed5315e31c7044c122fab"
S = "${WORKDIR}/git"
EXTRA_OECONF += "--with-abi-version=5"
UPSTREAM_CHECK_GITTAGREGEX = "(?P<pver>\d+(\.\d+)+(\+\d+)*)"
