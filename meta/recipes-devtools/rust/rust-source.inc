RUST_VERSION ?= "${@d.getVar('PV').split('-')[0]}"

SRC_URI += "https://static.rust-lang.org/dist/rustc-${RUST_VERSION}-src.tar.xz;name=rust \
            file://0001-Define-more-ioctl-codes-on-riscv32gc-unknown-linux-g.patch;patchdir=${RUSTSRC} \
            file://rust-oe-selftest.patch;patchdir=${RUSTSRC} \
            file://repro-issue-fix-with-cc-crate-hashmap.patch;patchdir=${RUSTSRC} \
            file://oeqa-selftest-Increase-timeout-in-process-sigpipe-ru.patch;patchdir=${RUSTSRC} \
            file://0001-src-core-build_steps-tool.rs-switch-off-lto-for-rust.patch;patchdir=${RUSTSRC} \
            file://revert-link-std-statically-in-rustc_driver-feature.patch;patchdir=${RUSTSRC} \
            file://0001-libunwind-Use-gcs-instead-of-gcs-target-attribute.patch;patchdir=${RUSTSRC} \
            file://0001-riscv32-Define-plain-syscalls-as-their-time64-varian.patch;patchdir=${RUSTSRC} \
            file://backport-fix-test-string-merging.patch;patchdir=${RUSTSRC} \
"
SRC_URI[rust.sha256sum] = "0b9d55610d8270e06c44f459d1e2b7918a5e673809c592abed9b9c600e33d95a"

RUSTSRC = "${UNPACKDIR}/rustc-${RUST_VERSION}-src"

UPSTREAM_CHECK_URI = "https://forge.rust-lang.org/infra/other-installation-methods.html"
UPSTREAM_CHECK_REGEX = "rustc-(?P<pver>\d+(\.\d+)+)-src"
