# resulttool - Show logs
#
# Copyright (c) 2019 Garmin International
#
# This program is free software; you can redistribute it and/or modify it
# under the terms and conditions of the GNU General Public License,
# version 2, as published by the Free Software Foundation.
#
# This program is distributed in the hope it will be useful, but WITHOUT
# ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
# FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
# more details.
#
import resulttool.resultutils as resultutils

def show_ptest(result, ptest, logger):
    if 'ptestresult.sections' in result:
        if ptest in result['ptestresult.sections'] and 'log' in result['ptestresult.sections'][ptest]:
            print(result['ptestresult.sections'][ptest]['log'])
            return 0

    print("ptest '%s' not found" % ptest)
    return 1

def log(args, logger):
    results = resultutils.load_resultsdata(args.source)
    for path in results:
        for res in results[path]:
            if 'result' not in results[path][res]:
                continue
            r = results[path][res]['result']

            if args.raw:
                if 'ptestresult.rawlogs' in r:
                    print(r['ptestresult.rawlogs']['log'])
                else:
                    print('Raw logs not found')
                    return 1

            for ptest in args.ptest:
                if not show_ptest(r, ptest, logger):
                    return 1

def register_commands(subparsers):
    """Register subcommands from this plugin"""
    parser = subparsers.add_parser('log', help='show logs',
                                         description='show the logs from test results',
                                         group='analysis')
    parser.set_defaults(func=log)
    parser.add_argument('source',
            help='the results file/directory/URL to import')
    parser.add_argument('--ptest', action='append', default=[],
            help='show logs for a ptest')
    parser.add_argument('--raw', action='store_true',
            help='show raw logs')

