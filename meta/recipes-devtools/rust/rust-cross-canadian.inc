
require rust-cross-canadian-common.inc

RUSTLIB_TARGET_PN = "rust-cross-canadian-rustlib-target-${TRANSLATED_TARGET_ARCH}"
RUSTLIB_HOST_PN = "rust-cross-canadian-rustlib-host-${TRANSLATED_TARGET_ARCH}"
RUSTLIB_SRC_PN = "rust-cross-canadian-src"
RUSTLIB_PKGS = "${RUSTLIB_SRC_PN} ${RUSTLIB_TARGET_PN} ${RUSTLIB_HOST_PN}"
PN = "rust-cross-canadian-${TRANSLATED_TARGET_ARCH}"

PACKAGES = "${RUSTLIB_PKGS} ${PN}"
RDEPENDS:${PN} += "${RUSTLIB_PKGS}"

# The default behaviour of x.py changed in 1.47+ so now we need to
# explicitly ask for the stage 2 compiler to be assembled.
do_compile () {
    rust_runx build --stage 2
}

create_sdk_wrapper () {
        file="$1"
        shift

        cat <<- EOF > "${file}"
		#!/bin/sh
		\$$1 \$@
		EOF

        chmod +x "$file"
}

do_install () {
    # Rust requires /usr/lib to contain the libs.
    # Similar story is with /usr/bin ruquiring  `lib` to be at the same level.
    # The required structure is retained for simplicity.
    SYS_LIBDIR=$(dirname ${D}${libdir})
    SYS_BINDIR=$(dirname ${D}${bindir})
    RUSTLIB_DIR=${SYS_LIBDIR}/${TARGET_SYS}/rustlib

    install -d "${SYS_BINDIR}"
    cp build/${SNAPSHOT_BUILD_SYS}/stage2/bin/* ${SYS_BINDIR}
    for i in ${SYS_BINDIR}/*; do
	chrpath -r "\$ORIGIN/../lib" ${i}
    done

    install -d "${D}${libdir}"
    cp -pRd build/${SNAPSHOT_BUILD_SYS}/stage2/lib/${TARGET_SYS}/*.so ${SYS_LIBDIR}
    cp -pRd build/${SNAPSHOT_BUILD_SYS}/stage2/lib/${TARGET_SYS}/rustlib ${RUSTLIB_DIR}

    for i in ${SYS_LIBDIR}/*.so; do
	chrpath -r "\$ORIGIN/../lib" ${i}
    done
    for i in ${RUSTLIB_DIR}/*/lib/*.so; do
	chrpath -d ${i}
    done

    install -m 0644 "${WORKDIR}/targets/${TARGET_SYS}.json" "${RUSTLIB_DIR}"

    SRC_DIR=${RUSTLIB_DIR}/src/rust
    install -d ${SRC_DIR}/src/llvm-project
    cp -R --no-dereference build/${SNAPSHOT_BUILD_SYS}/stage2/lib/rustlib/src/rust/src/llvm-project/libunwind ${SRC_DIR}/src/llvm-project
    cp -R --no-dereference build/${SNAPSHOT_BUILD_SYS}/stage2/lib/rustlib/src/rust/library ${SRC_DIR}
    cp --no-dereference build/${SNAPSHOT_BUILD_SYS}/stage2/lib/rustlib/src/rust/Cargo.lock ${SRC_DIR}
    # Remove executable bit from any files so then SDK doesn't try to relocate.
    chmod -R -x+X ${SRC_DIR}


    # Uses SDK's CC as linker so linked binaries works out of box.
    install -d ${SYS_BINDIR}
    create_sdk_wrapper "${SYS_BINDIR}/target-rust-ccld" "CC"

    ENV_SETUP_DIR=${D}${base_prefix}/environment-setup.d
    mkdir "${ENV_SETUP_DIR}"
    ENV_SETUP_SH="${ENV_SETUP_DIR}/rust.sh"

    cat <<- EOF > "${ENV_SETUP_SH}"
	export RUSTFLAGS="--sysroot=\$OECORE_NATIVE_SYSROOT/usr -C link-arg=--sysroot=\$OECORE_TARGET_SYSROOT -L\$OECORE_NATIVE_SYSROOT/usr/lib/${TARGET_SYS}/rustlib/${TARGET_SYS}/lib"
	export RUST_TARGET_PATH="\$OECORE_NATIVE_SYSROOT/usr/lib/${TARGET_SYS}/rustlib"
	EOF

    chown -R root.root ${D}

    ENV_SETUP_SH="${ENV_SETUP_DIR}/cargo.sh"
    cat <<- EOF > "${ENV_SETUP_SH}"
	export CARGO_HOME="\$OECORE_TARGET_SYSROOT/home/cargo"
	mkdir -p "\$CARGO_HOME"
        # Init the default target once, it might be otherwise user modified.
	if [ ! -f "\$CARGO_HOME/config" ]; then
		touch "\$CARGO_HOME/config"
		echo "[build]" >> "\$CARGO_HOME/config"
		echo 'target = "'${RUST_TARGET_SYS}'"' >> "\$CARGO_HOME/config"
		echo '# TARGET_SYS' >> "\$CARGO_HOME/config"
		echo '[target.'${RUST_TARGET_SYS}']' >> "\$CARGO_HOME/config"
		echo 'linker = "target-rust-ccld"' >> "\$CARGO_HOME/config"
    fi

	# Keep the below off as long as HTTP/2 is disabled.
	export CARGO_HTTP_MULTIPLEXING=false

	export CARGO_HTTP_CAINFO="\$OECORE_NATIVE_SYSROOT/etc/ssl/certs/ca-certificates.crt"
	EOF
}

PKG_SYS_LIBDIR = "${SDKPATHNATIVE}/usr/lib"
PKG_SYS_BINDIR = "${SDKPATHNATIVE}/usr/bin"
PKG_RUSTLIB_DIR = "${PKG_SYS_LIBDIR}/${TARGET_SYS}/rustlib"
FILES:${PN} = "${PKG_SYS_LIBDIR}/*.so ${PKG_SYS_BINDIR} ${base_prefix}/environment-setup.d"
FILES:${RUSTLIB_TARGET_PN} = "${PKG_RUSTLIB_DIR}/${TARGET_SYS} ${PKG_RUSTLIB_DIR}/${TARGET_SYS}.json"
FILES:${RUSTLIB_HOST_PN} = "${PKG_RUSTLIB_DIR}/${BUILD_ARCH}-unknown-linux-gnu"
FILES:${RUSTLIB_SRC_PN} = "${PKG_RUSTLIB_DIR}/src"

SUMMARY:${RUSTLIB_TARGET_PN} = "Rust cross canadian libaries for ${TARGET_SYS}"
SUMMARY:${RUSTLIB_HOST_PN} = "Rust cross canadian libaries for ${HOST_SYS}"
SUMMARY:${RUSTLIB_SRC_PN} = "Rust standard library sources for cross canadian toolchain"
SUMMARY:${PN} = "Rust crost canadian compiler"

