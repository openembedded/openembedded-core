RUST_VERSION ?= "${@d.getVar('PV').split('-')[0]}"
SRC_URI += "https://static.rust-lang.org/dist/rustc-${RUST_VERSION}-src.tar.xz;name=rust"
SRC_URI[rust.sha256sum] = "0dc176e34fae9871f855a6ba4cb30fa19d69c5b4428d29281a07419c4950715c"

SRC_URI:append:class-target:pn-libstd-rs = "\
    file://0001-Do-not-use-LFS64-on-linux-with-musl.patch;patchdir=../.. \
"

SRC_URI:append:class-target:pn-rust = " \
    file://getrandom-open64.patch \
    file://0001-Do-not-use-LFS64-on-linux-with-musl.patch \
    file://hardcodepaths.patch \
    file://crossbeam_atomic.patch"
SRC_URI:append:class-nativesdk:pn-nativesdk-rust = " file://hardcodepaths.patch"

RUSTSRC = "${WORKDIR}/rustc-${RUST_VERSION}-src"

UPSTREAM_CHECK_URI = "https://forge.rust-lang.org/infra/other-installation-methods.html"
UPSTREAM_CHECK_REGEX = "rustc-(?P<pver>\d+(\.\d+)+)-src"

# see recipes-devtools/gcc/gcc/0018-Add-ssp_nonshared-to-link-commandline-for-musl-targe.patch
# we need to link with ssp_nonshared on musl to avoid "undefined reference to `__stack_chk_fail_local'"
# when building MACHINE=qemux86 for musl
WRAPPER_TARGET_EXTRALD:libc-musl = "-lssp_nonshared"
