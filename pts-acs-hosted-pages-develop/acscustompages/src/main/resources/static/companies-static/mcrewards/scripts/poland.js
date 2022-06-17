$(document).ready(function(){


    $('.your-class').slick({
        centerMode: true,
        centerPadding: '60px',
        slidesToShow: 4,
        speed:750,
        index:3,
        dots: true,
        variableWidth: true,
        focusOnSelect:true,
        useCSS:true,
        slidesToScroll: 3,


		nextArrow: '<button class="btnNext"></button>',
		prevArrow: '<button class="btnPrev"></button>',



  responsive: [
    {
      breakpoint: 768,
      settings: {
        arrows: false,
        centerMode: true,
        centerPadding: '40px',
        slidesToShow: 3
      }
    },
    {
      breakpoint: 480,
      settings: {
        arrows: false,
        centerMode: true,
        centerPadding: '40px',
        slidesToShow: 1
      }
    }
  ]

});
$('#img1').click(function() {
   alert("test");
   if (data-index==1) {
       console.log("1")
   }
});

    $('.slider').slick({arrows: false})
    $('.left').click(function(){ $('.slider').slick('slickPrev');})
    $('.right').click(function(){ $('.slider').slick('slickNext');});

});