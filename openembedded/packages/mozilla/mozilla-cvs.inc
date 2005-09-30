do_fetch () {
	export MOZ_CO_DATE="${CVSDATE}"
	mkdir -p ${WORKDIR}
	cd ${WORKDIR}
	if [ ! -f ${DL_DIR}/mozilla_cvs-mirror.mozilla.org__${CVSDATE}.tar.gz ]; then
		cvs $CVSCOOPTS -d :pserver:anonymous@cvs-mirror.mozilla.org/cvsroot co mozilla/client.mk
		cd mozilla
		oe_runmake -f client.mk checkout
		cd ..
		tar czf ${DL_DIR}/mozilla_cvs-mirror.mozilla.org__${CVSDATE}.tar.gz mozilla
	fi
}
