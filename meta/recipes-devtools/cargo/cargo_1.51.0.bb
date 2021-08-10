require recipes-devtools/rust/rust-source-${PV}.inc
require recipes-devtools/rust/rust-snapshot-${PV}.inc
require cargo.inc

SRC_URI += "file://0001-Disable-http2.patch \
            file://riscv-march.patch;patchdir=../../.. \
            file://rv64gc.patch;patchdir=../../.. \
            "
