require ncurses.inc

SRC_URI += "file://0001-tic-hang.patch \
            file://0002-configure-reproducible.patch \
            file://config.cache \
"
# commit id corresponds to the revision in package version
SRCREV = "3740e18875f16dfe126484869882fb15c3fd1aea"
S = "${WORKDIR}/git"
EXTRA_OECONF += "--with-abi-version=5"
UPSTREAM_CHECK_GITTAGREGEX = "(?P<pver>\d+(\.\d+)+(\+\d+)*)"
