RUST_VERSION ?= "${@d.getVar('PV').split('-')[0]}"
SRC_URI += "https://static.rust-lang.org/dist/rustc-${RUST_VERSION}-src.tar.xz;name=rust"
SRC_URI[rust.sha256sum] = "0dc176e34fae9871f855a6ba4cb30fa19d69c5b4428d29281a07419c4950715c"

SRC_URI:append:class-target:pn-rust = " \
    file://hardcodepaths.patch \
    file://crossbeam_atomic.patch"
SRC_URI:append:class-nativesdk:pn-nativesdk-rust = " file://hardcodepaths.patch"

RUSTSRC = "${WORKDIR}/rustc-${RUST_VERSION}-src"

UPSTREAM_CHECK_URI = "https://forge.rust-lang.org/infra/other-installation-methods.html"
UPSTREAM_CHECK_REGEX = "rustc-(?P<pver>\d+(\.\d+)+)-src"
