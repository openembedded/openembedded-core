SRC_URI += "https://static.rust-lang.org/dist/rustc-${PV}-src.tar.xz;name=rust"
SRC_URI[rust.sha256sum] = "61fdcc8bc2530ee18f91318347e09b7cc07e9ca04b1c627c135ecc8dcb8f2ac5"

RUSTSRC = "${WORKDIR}/rustc-${PV}-src"

UPSTREAM_CHECK_URI = "https://forge.rust-lang.org/infra/other-installation-methods.html"
UPSTREAM_CHECK_REGEX = "rustc-(?P<pver>\d+(\.\d+)+)-src"
