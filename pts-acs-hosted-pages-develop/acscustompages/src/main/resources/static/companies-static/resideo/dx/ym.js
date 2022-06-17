class Resource {

    constructor(obj) {
            this.base = obj.base;
            this.token = obj.token;
            this.records = {};
    }

    AjaxCall(metod,url,dataIn,functionOk,functionFail,token){
        let that = this;
        console.log(dataIn);
        var request = $.ajax({
            headers: that.getHeaders(token),
            url: url,
            dataType: 'json',
            method: metod,
            data: dataIn,
            contentType: "application/json",
            xhrFields: { withCredentials: true },
            success: functionOk
        });
        request.fail(functionFail);
    }

    getHeaders(token){
        return {
            "x-services-token": token,
            "company": "resideo"
        }
    }



  
  loadRecords(param,doneRecords,fail){
  	let that = this;
  	  	// localhost:8080/v1/services/customResources/cusProducts/records/search/byParentcategoryfilter?parentCategory=Water Leak Detector&_lineStart=1&_lineCount=100
  	function done(d){
		that.records = d;
		doneRecords(d);
	}
	var url = this.base+"/customResources/cusProducts/records/search/byParentcategoryfilter?parentCategory="+param.parentCat+"&_lineStart="+param.start+"&_lineCount="+param.count;
	
  	that.AjaxCall("GET",url,"",done,fail,that.token);
  }
  
  
  
}











