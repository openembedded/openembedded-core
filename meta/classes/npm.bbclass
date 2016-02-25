DEPENDS_prepend = "nodejs-native "
S = "${WORKDIR}/npmpkg"

npm_do_compile() {
	# changing the home directory to the working directory, the .npmrc will
	# be created in this directory
	export HOME=${WORKDIR}
	npm config set dev false
	npm set cache ${WORKDIR}/npm_cache
	# clear cache before every build
	npm cache clear
	# Install pkg into ${S} without going to the registry
	npm --arch=${TARGET_ARCH} --production --no-registry install
}

npm_do_install() {
	mkdir -p ${D}${libdir}/node_modules/${PN}/
	cp -a ${S}/* ${D}${libdir}/node_modules/${PN}/ --no-preserve=ownership
}

FILES_${PN} += " \
    ${libdir}/node_modules/${PN} \
"

EXPORT_FUNCTIONS do_compile do_install
