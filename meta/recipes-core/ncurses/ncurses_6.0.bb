require ncurses.inc

REVISION = "20151024"

SRC_URI += "file://tic-hang.patch \
            file://config.cache \
"
# commit id corresponds to the above listed REVISION
SRCREV = "c6b034b4d69f8c9092c2a1b5d5bb718282b2a522"
S = "${WORKDIR}/git"
EXTRA_OECONF += "--with-abi-version=5"
UPSTREAM_CHECK_GITTAGREGEX = "(?P<pver>\d+(\.\d+)+(\+\d+)*)"
