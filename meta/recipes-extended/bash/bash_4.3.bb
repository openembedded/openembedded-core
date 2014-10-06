require bash.inc

# GPLv2+ (< 4.0), GPLv3+ (>= 4.0)
LICENSE = "GPLv3+"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"

SRC_URI = "${GNU_MIRROR}/bash/${BPN}-${PV}.tar.gz;name=tarball \
           ${GNU_MIRROR}/bash/bash-4.3-patches/bash43-001;apply=yes;striplevel=0;name=patch001 \
           ${GNU_MIRROR}/bash/bash-4.3-patches/bash43-002;apply=yes;striplevel=0;name=patch002 \
           ${GNU_MIRROR}/bash/bash-4.3-patches/bash43-003;apply=yes;striplevel=0;name=patch003 \
           ${GNU_MIRROR}/bash/bash-4.3-patches/bash43-004;apply=yes;striplevel=0;name=patch004 \
           ${GNU_MIRROR}/bash/bash-4.3-patches/bash43-005;apply=yes;striplevel=0;name=patch005 \
           ${GNU_MIRROR}/bash/bash-4.3-patches/bash43-006;apply=yes;striplevel=0;name=patch006 \
           ${GNU_MIRROR}/bash/bash-4.3-patches/bash43-007;apply=yes;striplevel=0;name=patch007 \
           ${GNU_MIRROR}/bash/bash-4.3-patches/bash43-008;apply=yes;striplevel=0;name=patch008 \
           ${GNU_MIRROR}/bash/bash-4.3-patches/bash43-009;apply=yes;striplevel=0;name=patch009 \
           ${GNU_MIRROR}/bash/bash-4.3-patches/bash43-010;apply=yes;striplevel=0;name=patch010 \
           ${GNU_MIRROR}/bash/bash-4.3-patches/bash43-011;apply=yes;striplevel=0;name=patch011 \
           ${GNU_MIRROR}/bash/bash-4.3-patches/bash43-012;apply=yes;striplevel=0;name=patch012 \
           ${GNU_MIRROR}/bash/bash-4.3-patches/bash43-013;apply=yes;striplevel=0;name=patch013 \
           ${GNU_MIRROR}/bash/bash-4.3-patches/bash43-014;apply=yes;striplevel=0;name=patch014 \
           ${GNU_MIRROR}/bash/bash-4.3-patches/bash43-015;apply=yes;striplevel=0;name=patch015 \
           ${GNU_MIRROR}/bash/bash-4.3-patches/bash43-016;apply=yes;striplevel=0;name=patch016 \
           ${GNU_MIRROR}/bash/bash-4.3-patches/bash43-017;apply=yes;striplevel=0;name=patch017 \
           ${GNU_MIRROR}/bash/bash-4.3-patches/bash43-018;apply=yes;striplevel=0;name=patch018 \
           ${GNU_MIRROR}/bash/bash-4.3-patches/bash43-019;apply=yes;striplevel=0;name=patch019 \
           ${GNU_MIRROR}/bash/bash-4.3-patches/bash43-020;apply=yes;striplevel=0;name=patch020 \
           ${GNU_MIRROR}/bash/bash-4.3-patches/bash43-021;apply=yes;striplevel=0;name=patch021 \
           ${GNU_MIRROR}/bash/bash-4.3-patches/bash43-022;apply=yes;striplevel=0;name=patch022 \
           ${GNU_MIRROR}/bash/bash-4.3-patches/bash43-023;apply=yes;striplevel=0;name=patch023 \
           ${GNU_MIRROR}/bash/bash-4.3-patches/bash43-024;apply=yes;striplevel=0;name=patch024 \
           ${GNU_MIRROR}/bash/bash-4.3-patches/bash43-025;apply=yes;striplevel=0;name=patch025 \
           ${GNU_MIRROR}/bash/bash-4.3-patches/bash43-026;apply=yes;striplevel=0;name=patch026 \
           ${GNU_MIRROR}/bash/bash-4.3-patches/bash43-027;apply=yes;striplevel=0;name=patch027 \
           ${GNU_MIRROR}/bash/bash-4.3-patches/bash43-028;apply=yes;striplevel=0;name=patch028 \
           ${GNU_MIRROR}/bash/bash-4.3-patches/bash43-029;apply=yes;striplevel=0;name=patch029 \
           ${GNU_MIRROR}/bash/bash-4.3-patches/bash43-030;apply=yes;striplevel=0;name=patch030 \
           file://execute_cmd.patch;striplevel=0 \
           file://mkbuiltins_have_stringize.patch \
           file://build-tests.patch \
           file://test-output.patch \
           file://run-ptest \
           "

SRC_URI[tarball.md5sum] = "81348932d5da294953e15d4814c74dd1"
SRC_URI[tarball.sha256sum] = "afc687a28e0e24dc21b988fa159ff9dbcf6b7caa92ade8645cc6d5605cd024d4"

SRC_URI[patch001.md5sum] = "1ab682b4e36afa4cf1b426aa7ac81c0d"
SRC_URI[patch001.sha256sum] = "ecb3dff2648667513e31554b3ad054ccd89fce38e33367c9459ac3a285153742"
SRC_URI[patch002.md5sum] = "8fc22cf50ec85da00f6af3d66f7ddc1b"
SRC_URI[patch002.sha256sum] = "eee7cd7062ab29a9e4f02924d9c367264dcb8b162703f74ff6eb8f175a91502b"
SRC_URI[patch003.md5sum] = "a41728eca78858758e26b5dea64ae506"
SRC_URI[patch003.sha256sum] = "000e6eac50cd9053ce0630db01239dcdead04a2c2c351c47e2b51dac1ac1087d"
SRC_URI[patch004.md5sum] = "bf8d53d227829d67235927689a03cc7a"
SRC_URI[patch004.sha256sum] = "5ea0a42c6506720d26e6d3c5c358e9a0d49f6f189d69a8ed34d5935964821338"
SRC_URI[patch005.md5sum] = "c0c00935c8b8ffff76e8ab77e7be7d15"
SRC_URI[patch005.sha256sum] = "1ac83044032b9f5f11aeca8a344ae3c524ec2156185d3adbb8ad3e7a165aa3fa"
SRC_URI[patch006.md5sum] = "6f01e364cd092faa28dd7119f47ddb5f"
SRC_URI[patch006.sha256sum] = "a0648ee72d15e4a90c8b77a5c6b19f8d89e28c1bc881657d22fe26825f040213"
SRC_URI[patch007.md5sum] = "dcf471d222bcd83283d3094e6ceeb6f8"
SRC_URI[patch007.sha256sum] = "1113e321c59cf6a8648a36245bbe4217cf8acf948d71e67886dad7d486f8f3a3"
SRC_URI[patch008.md5sum] = "f7553416646dc26c266454c78a916d36"
SRC_URI[patch008.sha256sum] = "9941a98a4987192cc5ce3d45afe879983cad2f0bec96d441a4edd9033767f95e"
SRC_URI[patch009.md5sum] = "7e73d2151f4064b484a4ba2c4b09960e"
SRC_URI[patch009.sha256sum] = "c0226d6728946b2f53cdebf090bcd1c01627f01fee03295768605caa80bb40a5"
SRC_URI[patch010.md5sum] = "a275463d21735bb6d7161f9fbd320d8f"
SRC_URI[patch010.sha256sum] = "ce05799c0137314c70c7b6ea0477c90e1ac1d52e113344be8e32fa5a55c9f0b7"
SRC_URI[patch011.md5sum] = "c17103ee20420d77e46b224c8d3fceda"
SRC_URI[patch011.sha256sum] = "7c63402cdbc004a210f6c1c527b63b13d8bb9ec9c5a43d5c464a9010ff6f7f3b"
SRC_URI[patch012.md5sum] = "3e2a057a19d02b3f92a3a09eacbc03ae"
SRC_URI[patch012.sha256sum] = "3e1379030b35fbcf314e9e7954538cf4b43be1507142b29efae39eef997b8c12"
SRC_URI[patch013.md5sum] = "fb377143a996d4ff087a2771bc8332f9"
SRC_URI[patch013.sha256sum] = "bfa8ca5336ab1f5ef988434a4bdedf71604aa8a3659636afa2ce7c7446c42c79"
SRC_URI[patch014.md5sum] = "1a1aaecc99a9d0cbc310e8e247dcc8b6"
SRC_URI[patch014.sha256sum] = "5a4d6fa2365b6eb725a9d4966248b5edf7630a4aeb3fa8d526b877972658ac13"
SRC_URI[patch015.md5sum] = "4f04387458a3c1b4d460d199f49991a8"
SRC_URI[patch015.sha256sum] = "13293e8a24e003a44d7fe928c6b1e07b444511bed2d9406407e006df28355e8d"
SRC_URI[patch016.md5sum] = "90e759709720c4f877525bebc9d5dc06"
SRC_URI[patch016.sha256sum] = "92d60bcf49f61bd7f1ccb9602bead6f2c9946d79dea0e5ec0589bb3bfa5e0773"
SRC_URI[patch017.md5sum] = "11e4046e1b86070f6adbb7ffc89641be"
SRC_URI[patch017.sha256sum] = "1267c25c6b5ba57042a7bb6c569a6de02ffd0d29530489a16666c3b8a23e7780"
SRC_URI[patch018.md5sum] = "cd5a9b46f5bea0dc0248c93c7dfac011"
SRC_URI[patch018.sha256sum] = "7aa8b40a9e973931719d8cc72284a8fb3292b71b522db57a5a79052f021a3d58"
SRC_URI[patch019.md5sum] = "cff4dc024d9d3456888aaaf8a36ca774"
SRC_URI[patch019.sha256sum] = "a7a91475228015d676cafa86d2d7aa9c5d2139aa51485b6bbdebfdfbcf0d2d23"
SRC_URI[patch020.md5sum] = "167839c5f147347f4a03d88ab97ff787"
SRC_URI[patch020.sha256sum] = "ca5e86d87f178128641fe91f2f094875b8c1eb2de9e0d2e9154f5d5cc0336c98"
SRC_URI[patch021.md5sum] = "1d350671c48dec30b34d8b81f09cd79d"
SRC_URI[patch021.sha256sum] = "41439f06883e6bd11c591d9d5e9ae08afbc2abd4b935e1d244b08100076520a9"
SRC_URI[patch022.md5sum] = "11c349af66a55481a3215ef2520bec36"
SRC_URI[patch022.sha256sum] = "fd4d47bb95c65863f634c4706c65e1e3bae4ee8460c72045c0a0618689061a88"
SRC_URI[patch023.md5sum] = "b3cb0d80fd0c47728264405cbb3b23c7"
SRC_URI[patch023.sha256sum] = "9ac250c7397a8f53dbc84dfe790d2a418fbf1fe090bcece39b4a5c84a2d300d4"
SRC_URI[patch024.md5sum] = "b5ea5600942acceb4b6f07313d2de74e"
SRC_URI[patch024.sha256sum] = "3b505882a0a6090667d75824fc919524cd44cc3bd89dd08b7c4e622d3f960f6c"
SRC_URI[patch025.md5sum] = "193c06f578d38ffdbaebae9c51a7551f"
SRC_URI[patch025.sha256sum] = "1e5186f5c4a619bb134a1177d9e9de879f3bb85d9c5726832b03a762a2499251"
SRC_URI[patch026.md5sum] = "922578e2be7ed03729454e92ee8d3f3a"
SRC_URI[patch026.sha256sum] = "2ecc12201b3ba4273b63af4e9aad2305168cf9babf6d11152796db08724c214d"
SRC_URI[patch027.md5sum] = "8ff6948b16f2db5c29b1b9ae1085bbe7"
SRC_URI[patch027.sha256sum] = "1eb76ad28561d27f7403ff3c76a36e932928a4b58a01b868d663c165f076dabe"
SRC_URI[patch028.md5sum] = "dd51fa67913b5dca45a702b672b3323f"
SRC_URI[patch028.sha256sum] = "e8b0dbed4724fa7b9bd8ff77d12c7f03da0fbfc5f8251ef5cb8511eb082b469d"
SRC_URI[patch029.md5sum] = "0729364c977ef4271e9f8dfafadacf67"
SRC_URI[patch029.sha256sum] = "4cc4a397fe6bc63ecb97d030a4e44258ef2d4e076d0e90c77782968cc43d6292"
SRC_URI[patch030.md5sum] = "efb709fdb1368945513de23ccbfae053"
SRC_URI[patch030.sha256sum] = "85434f8a2f379d0c49a3ff6d9ffa12c8b157188dd739e556d638217d2a58385b"

BBCLASSEXTEND = "nativesdk"
