SRC_URI[rust.sha256sum] = "aec85a7c1f40b7a40818a58ae13632f8a12cfaa4d3e2a10957d0e9d16dfdd556"
SRC_URI += "https://static.rust-lang.org/dist/rustc-${PV}-src.tar.xz;name=rust"

RUSTSRC = "${WORKDIR}/rustc-${PV}-src"

UPSTREAM_CHECK_URI = "https://forge.rust-lang.org/infra/other-installation-methods.html"
UPSTREAM_CHECK_REGEX = "rustc-(?P<pver>\d+(\.\d+)+)-src"
