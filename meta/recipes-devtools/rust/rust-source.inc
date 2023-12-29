RUST_VERSION ?= "${@d.getVar('PV').split('-')[0]}"

SRC_URI += "https://static.rust-lang.org/dist/rustc-${RUST_VERSION}-src.tar.xz;name=rust \
            file://hardcodepaths.patch;patchdir=${RUSTSRC} \
            file://0001-Do-not-use-LFS64-on-linux-with-musl.patch;patchdir=${RUSTSRC} \
            file://zlib-off64_t.patch;patchdir=${RUSTSRC} \
            file://0001-musl-Define-SOCK_SEQPACKET-in-common-place.patch;patchdir=${RUSTSRC} \
            file://0001-Revert-Map-source-absolute-paths-to-OUT_DIR-as-relat.patch;patchdir=${RUSTSRC} \
"
SRC_URI[rust.sha256sum] = "d307441f8ee78a7e94f72cb5c81383822f13027f79e67a5551bfd2c2d2db3014"

RUSTSRC = "${WORKDIR}/rustc-${RUST_VERSION}-src"

UPSTREAM_CHECK_URI = "https://forge.rust-lang.org/infra/other-installation-methods.html"
UPSTREAM_CHECK_REGEX = "rustc-(?P<pver>\d+(\.\d+)+)-src"
