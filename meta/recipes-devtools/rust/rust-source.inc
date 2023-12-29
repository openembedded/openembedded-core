RUST_VERSION ?= "${@d.getVar('PV').split('-')[0]}"

SRC_URI += "https://static.rust-lang.org/dist/rustc-${RUST_VERSION}-src.tar.xz;name=rust \
            file://hardcodepaths.patch;patchdir=${RUSTSRC} \
            file://0001-Don-t-use-LFS64-symbols-on-musl.patch;patchdir=${RUSTSRC} \
            file://zlib-off64_t.patch;patchdir=${RUSTSRC} \
            file://0001-musl-Define-SOCK_NONBLOCK-with-O_NONBLOCK.patch;patchdir=${RUSTSRC} \
            file://0002-musl-riscv32-Define-F_SETLK-F_SETLKW-and-fix-F_GETLK.patch;patchdir=${RUSTSRC} \
            file://0003-musl-Move-F_OFD_GETLK-F_OFD_SETLK-and-F_OFD_SETLKW-t.patch;patchdir=${RUSTSRC} \
            file://0004-musl-Define-O_LARGEFILE-for-riscv32.patch;patchdir=${RUSTSRC} \
            file://0005-musl-Define-SOCK_SEQPACKET-in-common-place.patch;patchdir=${RUSTSRC} \
            file://0001-Revert-Map-source-absolute-paths-to-OUT_DIR-as-relat.patch;patchdir=${RUSTSRC} \
"
SRC_URI[rust.sha256sum] = "23705e38c1a37acfd7fbb921c5dd8772619476e80d0b3b39ac8eb45bc0c33187"

RUSTSRC = "${WORKDIR}/rustc-${RUST_VERSION}-src"

UPSTREAM_CHECK_URI = "https://forge.rust-lang.org/infra/other-installation-methods.html"
UPSTREAM_CHECK_REGEX = "rustc-(?P<pver>\d+(\.\d+)+)-src"
