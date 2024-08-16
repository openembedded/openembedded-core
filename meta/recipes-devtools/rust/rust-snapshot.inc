## This is information on the rust-snapshot (binary) used to build our current release.
## snapshot info is taken from rust/src/stage0.json
## Rust is self-hosting and bootstraps itself with a pre-built previous version of itself.
## The exact (previous) version that has been used is specified in the source tarball.
## The version is replicated here.

SNAPSHOT_VERSION = "1.77.0"

SRC_URI[cargo-snapshot-aarch64.sha256sum] = "0833e133e2b98d840c5180a3dabc49c0de9895c54055dfee92fa94d2a12196d5"
SRC_URI[clippy-snapshot-aarch64.sha256sum] = "a60b58615747ac868f4d23d711d0386685beaeb597bc2e2bb7ce49a2b4826beb"
SRC_URI[rust-std-snapshot-aarch64.sha256sum] = "b6c551baf0cc9a32582f2362379b596f33c1bc93ae520d5648e9138de1b4b90b"
SRC_URI[rustc-snapshot-aarch64.sha256sum] = "ca7caef0b4e32f769edf4d7d596d0da9c3d562dd8233bd9068d3ba6cfe8dfe48"

SRC_URI[cargo-snapshot-i686.sha256sum] = "eaf3461608760821fdf0beca8f68eaa743fc9a7fcc1725bfb143e57b815d123e"
SRC_URI[clippy-snapshot-i686.sha256sum] = "b4998f498080af2fa83d6e46921c63a05115ee8113fdbd6654b42678dbdeeeeb"
SRC_URI[rust-std-snapshot-i686.sha256sum] = "332b4652fc5f37379ff8f820a6affea6500350d0669f72cf4fdec9b8900d66ea"
SRC_URI[rustc-snapshot-i686.sha256sum] = "1d15d1558d1c97072c1ecfac9f4466022c4afa482e06efdb5cf4eeebe940a186"

SRC_URI[cargo-snapshot-loongarch64.sha256sum] = "3952bf1766b7ef04218ab990066abe7598af2dc6fd6ce417569637ee919990bd"
SRC_URI[clippy-snapshot-loongarch64.sha256sum] = "b6bf201285610ff01e262c27f3e663200d36839ce5176b5cfdf7bd1c02a139b3"
SRC_URI[rust-std-snapshot-loongarch64.sha256sum] = "bd5a72c959f437b8245df6aa59171d4678ba7a47b963ec0552032a88247123ce"
SRC_URI[rustc-snapshot-loongarch64.sha256sum] = "f629ac5186026fe46b8656b0c91cf6ae8059c7f4e0c62c5d73e00aac51c0d1d4"

SRC_URI[cargo-snapshot-powerpc.sha256sum] = "daf08e82bb631650e4ed44cced4a943ef368fa89ab4a098ee11e6dac0f5b2f55"
SRC_URI[clippy-snapshot-powerpc.sha256sum] = "8e092ec41ad2edf8ebfe06cf476638ff46a77279d3b902461a0627cc5fc059f8"
SRC_URI[rust-std-snapshot-powerpc.sha256sum] = "6468f16f8430c7b34d3bdfb11afd6843531859b69258f3ccc46f304499603fbe"
SRC_URI[rustc-snapshot-powerpc.sha256sum] = "2017726dcd5ffef41fc63061ace7292fb2311d42b3130288d7706fbcfc1497e2"

SRC_URI[cargo-snapshot-powerpc64.sha256sum] = "4a93b099561403caeb0ff7706a2def51ad3528acb4782953ef2e9a6de56eda7f"
SRC_URI[clippy-snapshot-powerpc64.sha256sum] = "eecf9d2e4a7ebff9e6719b53bb0de196edb89d2faa3a395934b4156b11d0a23d"
SRC_URI[rust-std-snapshot-powerpc64.sha256sum] = "7cc586154e92305d5f89276107e1175fdf1f2cd95739aa6926cab4774c9ffe5d"
SRC_URI[rustc-snapshot-powerpc64.sha256sum] = "60d1b3cc02e48489638bfd976c1c979a40c92452cc93d0fc4ff4c16ba64c8f86"

SRC_URI[cargo-snapshot-powerpc64le.sha256sum] = "a4c4421fe3037522908de081cc0577baaf75e7b8c532616ce79dd032a42c60e8"
SRC_URI[clippy-snapshot-powerpc64le.sha256sum] = "9b46217dc6cf5c10cd65d343802c2da0f6f3a65d13ca10730258de9a74e1b7f1"
SRC_URI[rust-std-snapshot-powerpc64le.sha256sum] = "deffffaaa58b9464719fe4609ccad5db14e482aa48c3dcdb0a8aea62bb82332c"
SRC_URI[rustc-snapshot-powerpc64le.sha256sum] = "381bd57fd07e6f93e56893091b8aaf0d097b9957d655c2dfbaeff35b2305ea29"

SRC_URI[cargo-snapshot-riscv64gc.sha256sum] = "20d5fe02ccfd86220d0fb5f170c44bc05af4bfdd8e9060f2e084b0af5faf53a7"
SRC_URI[clippy-snapshot-riscv64gc.sha256sum] = "d1373618e6c4b702a4d00089f26ee048969e3b3bea0116bae3a9ba75508c0d98"
SRC_URI[rust-std-snapshot-riscv64gc.sha256sum] = "94443ac959cacd1bab96af3c690794c3ac4e05780c09dacdc85073134bf70c69"
SRC_URI[rustc-snapshot-riscv64gc.sha256sum] = "186d39a91756738fd5ab90acfb944c6206b30374a780600b71d98f2dc2e509cc"

SRC_URI[cargo-snapshot-s390x.sha256sum] = "f5122b1ba1f43d40013d6f24495e1a289b3c70bfc4d09d2e5e581d9f0c051786"
SRC_URI[clippy-snapshot-s390x.sha256sum] = "9d1d789feea21bb3e4d0bf82e6bdfa67e1f33236186e6c9f0023219dd1ca2f76"
SRC_URI[rust-std-snapshot-s390x.sha256sum] = "680e6c5ea712cb713c65c8b63ec44c441d60b5dc2bb7a3031088175dee33ba0b"
SRC_URI[rustc-snapshot-s390x.sha256sum] = "b80743fb83e3f3a61f9267fde1d39709e009d8e25b85a7bc45f915c6b36fa836"

SRC_URI[cargo-snapshot-x86_64.sha256sum] = "0af971f126e0307d4e4d974f0e9c33fd1c2923274b14a0861823b5a019e8faf5"
SRC_URI[clippy-snapshot-x86_64.sha256sum] = "f4bbb77674bc229b021b946dcf1d48a80d3d2fcfc7e10052cc01e2bfcc2769cd"
SRC_URI[rust-std-snapshot-x86_64.sha256sum] = "e5fec840744c3ff63611f6801de80f5ec8e099fa8225bf46d064810a9545e9b5"
SRC_URI[rustc-snapshot-x86_64.sha256sum] = "89397dddb99733bad987e9fdc9d985a150af2e39c81766099e584d93991f5752"

SRC_URI[rust-std-snapshot-i586.sha256sum] = "97a22f0fc78a1731b3e6795e76e94f958702a6b4c3ff5b627c63e33455c45825"

SRC_URI[rust-std-snapshot-sparc64.sha256sum] = "c2df78ff30b84ed80a140820356c323ca3c707529f1caeeae37a5419c02cc7be"

SRC_URI += " \
    ${RUST_DIST_SERVER}/dist/${RUST_STD_SNAPSHOT}.tar.xz;name=rust-std-snapshot-${RUST_BUILD_ARCH};subdir=rust-snapshot-components \
    ${RUST_DIST_SERVER}/dist/${RUSTC_SNAPSHOT}.tar.xz;name=rustc-snapshot-${RUST_BUILD_ARCH};subdir=rust-snapshot-components \
    ${RUST_DIST_SERVER}/dist/${CARGO_SNAPSHOT}.tar.xz;name=cargo-snapshot-${RUST_BUILD_ARCH};subdir=rust-snapshot-components \
"

RUST_DIST_SERVER = "https://static.rust-lang.org"
RUST_STD_SNAPSHOT = "rust-std-${SNAPSHOT_VERSION}-${RUST_BUILD_ARCH}-unknown-linux-gnu"
RUSTC_SNAPSHOT = "rustc-${SNAPSHOT_VERSION}-${RUST_BUILD_ARCH}-unknown-linux-gnu"
CARGO_SNAPSHOT = "cargo-${SNAPSHOT_VERSION}-${RUST_BUILD_ARCH}-unknown-linux-gnu"
