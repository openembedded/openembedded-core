# test result tool - merge multiple testresults.json files
#
# Copyright (c) 2019, Intel Corporation.
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
from resulttool.resultsutils import load_json_file, get_dict_value, dump_json_data
import os
import json

class ResultsMerge(object):

    def get_test_results(self, logger, file, result_id):
        results = load_json_file(file)
        if result_id:
            result = get_dict_value(logger, results, result_id)
            if result:
                return {result_id: result}
            return result
        return results

    def merge_results(self, base_results, target_results):
        for k in target_results:
            base_results[k] = target_results[k]
        return base_results

    def _get_write_dir(self):
        basepath = os.environ['BUILDDIR']
        return basepath + '/tmp/'

    def dump_merged_results(self, results, output_dir):
        file_output_dir = output_dir if output_dir else self._get_write_dir()
        dump_json_data(file_output_dir, 'testresults.json', results)
        print('Successfully merged results to: %s' % os.path.join(file_output_dir, 'testresults.json'))

    def run(self, logger, base_result_file, target_result_file, target_result_id, output_dir):
        base_results = self.get_test_results(logger, base_result_file, '')
        target_results = self.get_test_results(logger, target_result_file, target_result_id)
        if base_results and target_results:
            merged_results = self.merge_results(base_results, target_results)
            self.dump_merged_results(merged_results, output_dir)

def merge(args, logger):
    merge = ResultsMerge()
    merge.run(logger, args.base_result_file, args.target_result_file, args.target_result_id, args.output_dir)
    return 0

def register_commands(subparsers):
    """Register subcommands from this plugin"""
    parser_build = subparsers.add_parser('merge', help='merge test results',
                                         description='merge results from multiple files',
                                         group='setup')
    parser_build.set_defaults(func=merge)
    parser_build.add_argument('base_result_file',
                              help='base result file provide the base result set')
    parser_build.add_argument('target_result_file',
                              help='target result file provide the target result set for merging into the '
                                   'base result set')
    parser_build.add_argument('-t', '--target-result-id', default='',
                              help='(optional) default merge all result sets available from target to base '
                                   'unless specific target result id was provided')
    parser_build.add_argument('-o', '--output-dir', default='',
                              help='(optional) default write merged results to <poky>/build/tmp/ unless specific  '
                                   'output directory was provided')
