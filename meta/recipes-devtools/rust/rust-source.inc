RUST_VERSION ?= "${@d.getVar('PV').split('-')[0]}"

SRC_URI += "https://static.rust-lang.org/dist/rustc-${RUST_VERSION}-src.tar.xz;name=rust \
            file://hardcodepaths.patch;patchdir=${RUSTSRC} \
            file://getrandom-open64.patch;patchdir=${RUSTSRC} \
            file://0001-Do-not-use-LFS64-on-linux-with-musl.patch;patchdir=${RUSTSRC} \
            file://zlib-off64_t.patch;patchdir=${RUSTSRC} \
            file://0001-musl-Define-SOCK_SEQPACKET-in-common-place.patch;patchdir=${RUSTSRC} \
"
SRC_URI[rust.sha256sum] = "371af0fbe04051e20a74dbea6d4e4e548f10f15309c49cae2688afb882b6c7f1"

RUSTSRC = "${WORKDIR}/rustc-${RUST_VERSION}-src"

UPSTREAM_CHECK_URI = "https://forge.rust-lang.org/infra/other-installation-methods.html"
UPSTREAM_CHECK_REGEX = "rustc-(?P<pver>\d+(\.\d+)+)-src"
