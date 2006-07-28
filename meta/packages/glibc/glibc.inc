
do_rig_locales() {
	# If indicated, only build a limited selection of locales
	if [ "x${LIMIT_BUILT_LOCALES}" != "${LIMIT_BUILT_LOCALES}" ]; then
		INFILE="${S}/localedata/SUPPORTED"
		OUTFILE="${S}/localedata/SUPPORTED.tmp"
		head -n 3 $INFILE > $OUTFILE
		for i in ${LIMIT_BUILT_LOCALES}; do
			echo 
			grep $i $INFILE >> $OUTFILE
		done
		head --lines=-1 $OUTFILE > $INFILE
		tail --lines=1 $OUTFILE | sed 's#\\##' >> $INFILE
	fi
}

addtask rig_locales before do_compile after do_configure
