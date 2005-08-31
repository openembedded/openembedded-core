DEPENDS_prepend = "sip-native python-sip "

#EXTRA_SIPTAGS = "-tWS_QWS -tQtPE_1_6_0 -tQt_2_3_1"

sip_do_generate() {
	if [ -z "${SIP_MODULES}" ]; then 
		MODULES="`ls sip/*mod.sip`"
	else
		MODULES="${SIP_MODULES}"
	fi

	if [ -z "$MODULES" ]; then
		die "SIP_MODULES not set and no modules found in $PWD"
        else
		oenote "using modules '${SIP_MODULES}' and tags '${EXTRA_SIPTAGS}'"
	fi

	if [ -z "${EXTRA_SIPTAGS}" ]; then
		die "EXTRA_SIPTAGS needs to be set!"
	else
		SIPTAGS="${EXTRA_SIPTAGS}"
	fi

	if [ ! -z "${SIP_FEATURES}" ]; then
		FEATURES="-z ${SIP_FEATURES}"
		oenote "sip feature file: ${SIP_FEATURES}"
	fi

	for module in $MODULES
	do
        	install -d ${module}/
		oenote "calling 'sip -I sip -I ${STAGING_SIPDIR} ${SIPTAGS} ${FEATURES} -c ${module} -b ${module}/${module}.pro.in sip/${module}/${module}mod.sip'"
		sip -I ${STAGING_SIPDIR} -I sip ${SIPTAGS} ${FEATURES} -c ${module} -b ${module}/${module}.sbf sip/${module}/${module}mod.sip \
		|| die "Error calling sip on ${module}"
		cat ${module}/${module}.sbf 	| sed s,target,TARGET, \
						| sed s,sources,SOURCES, \
						| sed s,headers,HEADERS, \
						| sed s,"moc_HEADERS =","HEADERS +=", \
		>${module}/${module}.pro
		echo "TEMPLATE=lib" >>${module}/${module}.pro
		[ "${module}" = "qt" ] 		&& echo "" 		>>${module}/${module}.pro
		[ "${module}" = "qtcanvas" ] 	&& echo ""		>>${module}/${module}.pro
		[ "${module}" = "qttable" ] 	&& echo ""		>>${module}/${module}.pro
		[ "${module}" = "qwt" ] 	&& echo ""		>>${module}/${module}.pro
		[ "${module}" = "qtpe" ]        && echo ""		>>${module}/${module}.pro
		[ "${module}" = "qtpe" ]        && echo "LIBS+=-lqpe"	>>${module}/${module}.pro
		true
	done
}

EXPORT_FUNCTIONS do_generate

addtask generate after do_unpack do_patch before do_configure
