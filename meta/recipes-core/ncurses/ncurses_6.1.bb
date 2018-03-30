require ncurses.inc

SRC_URI += "file://0001-tic-hang.patch \
            file://0002-configure-reproducible.patch \
            file://config.cache \
"
# commit id corresponds to the revision in package version
SRCREV = "518fe9f913a0bfb80f1e366c9fcccf23e79dc69b"
S = "${WORKDIR}/git"
EXTRA_OECONF += "--with-abi-version=5"
UPSTREAM_CHECK_GITTAGREGEX = "(?P<pver>\d+(\.\d+)+(\+\d+)*)"
